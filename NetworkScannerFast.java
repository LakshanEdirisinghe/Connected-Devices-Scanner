import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class NetworkScannerFast {

    private static final int TOTAL_IPS = 254; // 1–254
    static {
        clearScreen();
    }

    public static void main(String[] args) throws Exception {
        
        int times=0;

        for (int x = 0; x < 3; x++) {

            String localIP = getLocalIPAddress();
            String subnet = getSubnet(localIP);
            String routerIP = subnet + ".1";

            System.out.println("Your IP: " + localIP);
            System.out.println("Subnet: " + subnet + ".x");
            System.out.println("Scanning for devices...\n");

            Set<String> liveHosts = ConcurrentHashMap.newKeySet();
            ExecutorService executor = Executors.newFixedThreadPool(50);
            AtomicInteger completed = new AtomicInteger(0);

            for (int i = 1; i <= TOTAL_IPS; i++) {
                final String host = subnet + "." + i; // must be final for lambda
                executor.submit(() -> {
                    try {
                        if (InetAddress.getByName(host).isReachable(50)) {
                            liveHosts.add(host);
                        }
                    } catch (IOException ignored) {
                    }
                    printProgress(completed.incrementAndGet());
                });
            }

            executor.shutdown();
            executor.awaitTermination(20, TimeUnit.SECONDS); // increase timeout if needed

            liveHosts.addAll(getARPTable());

            int totOfOther = 0;

            System.out.println("\nDevices found:\n");
            for (String host : new TreeSet<>(liveHosts)) {
                if (isValidDeviceIP(host, subnet)) {
                    if (host.equals(localIP)) {
                        System.out.println("$ " + host + "  [This Device]\n");
                    } else if (host.equals(routerIP)) {
                        System.out.println("# " + host + "  [Router]");
                    } else {
                        System.out.println(">" + host + "  [Other Device]");
                        totOfOther++;
                    }
                }
            }

            System.out.println("\n>> "+(++times )+ " Times Scan Completed");
            System.out.println("## " + totOfOther + " Other Devices are Found\n ");

            if (x < 2) {
                Thread.sleep(2000); // wait a moment to show results
                clearScreen();
            }
        }
    }

    private static void printProgress(int completed) {
        int width = 30;
        int progress = (completed * width) / TOTAL_IPS;
        String bar = "=".repeat(progress) + " ".repeat(width - progress);
        System.out.printf("\r[%s] %d%%", bar, (completed * 100) / TOTAL_IPS);
        if (completed == TOTAL_IPS)
            System.out.println();
    }

    private static String getLocalIPAddress() throws Exception {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getLocalAddress().getHostAddress();
        }
    }

    private static String getSubnet(String ip) {
        return ip.substring(0, ip.lastIndexOf("."));
    }

    private static Set<String> getARPTable() throws IOException {
        Set<String> arpHosts = new HashSet<>();
        Process p = Runtime.getRuntime().exec("arp -a");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length >= 2) {
                    String ip = parts[0].replace("(", "").replace(")", "");
                    if (ip.matches("\\d+\\.\\d+\\.\\d+\\.\\d+")) {
                        arpHosts.add(ip);
                    }
                }
            }
        }
        return arpHosts;
    }

    private static boolean isValidDeviceIP(String ip, String subnet) {
        if (!ip.startsWith(subnet + "."))
            return false;
        String lastOctet = ip.substring(ip.lastIndexOf(".") + 1);
        try {
            int octet = Integer.parseInt(lastOctet);
            return octet > 0 && octet < 255;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // ✅ Clear screen method
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
