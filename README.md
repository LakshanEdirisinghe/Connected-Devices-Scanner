# 🖧 NetworkScannerFast

A lightweight and fast **Java-based Network Scanner** that discovers
devices connected to your local network by scanning IP addresses.

This tool is useful for checking **who is connected to your Wi-Fi**,
troubleshooting network issues, and learning about local networking.

------------------------------------------------------------------------

## 🚀 Features

-   ⚡ Fast concurrent scanning of multiple IPs.\
-   📡 Detects active devices in the subnet.\
-   📋 Displays discovered IP addresses.\
-   🖥️ Works on Windows, macOS, and Linux (with Java installed).\
-   🔒 No third-party libraries required --- runs with pure Java.

------------------------------------------------------------------------

## 🛠️ Requirements

-   **Java 8+** installed on your system.\
-   A local network connection (e.g., Wi-Fi or LAN).

Check your Java version with:

``` bash
java -version
```

------------------------------------------------------------------------

## 📥 Installation

1.  Clone this repository:

    ``` bash
    git clone https://github.com/your-username/NetworkScannerFast.git
    cd NetworkScannerFast
    ```

2.  Compile the program:

    ``` bash
    javac NetworkScannerFast.java
    ```

3.  Run the scanner:

    ``` bash
    java NetworkScannerFast
    ```

------------------------------------------------------------------------

## ⚙️ How It Works

1.  The program scans a range of IP addresses in your **local subnet**.

    -   For example: `192.168.1.1` → `192.168.1.254`

2.  Each IP is pinged in parallel using Java's multithreading.

3.  If a device responds, it is marked as **connected**.

4.  Results are printed in the terminal.

------------------------------------------------------------------------

## 📊 Example Output

``` bash
Scanning your local network...

192.168.1.1  ✅  (Active)
192.168.1.5  ✅  (Active)
192.168.1.12 ❌  (No Response)
192.168.1.20 ✅  (Active)

Scan complete. Devices found: 3
```
<img width="586" height="442" alt="image" src="https://github.com/user-attachments/assets/b8fdc292-f518-42e5-bf1f-4555996b41ec" />


------------------------------------------------------------------------

## 🔍 Viewing Connected Devices

-   The scanner automatically lists all **responsive devices** in your
    subnet.\
-   Each entry shows the **IP address** and whether it is reachable.\
-   Optionally, you can extend the program to also fetch **hostnames**
    or **MAC addresses**.

------------------------------------------------------------------------

## 🏗️ Possible Improvements

-   Add **MAC address** detection.\
-   Identify device manufacturer.\
-   Export results to a file (`.txt` or `.csv`).\
-   Web-based dashboard for real-time scans.

------------------------------------------------------------------------

## 📜 License

This project is open-source under the **MIT License** --- free to use
and modify.
