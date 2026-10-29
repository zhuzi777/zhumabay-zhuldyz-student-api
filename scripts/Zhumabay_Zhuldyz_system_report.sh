#!/bin/bash
# System monitoring report — Zhuldyz Zhumabay (IT2-2308)
# Usage: ./scripts/Zhumabay_Zhuldyz_system_report.sh [log_file]
# Works on Linux and macOS. Output is shown on screen AND appended to the log (tee -a).

STUDENT_NAME="Zhuldyz Zhumabay"
PROJECT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
LOG_FILE="${1:-$PROJECT_DIR/logs/Zhumabay_Zhuldyz_system_report.log}"

mkdir -p "$(dirname "$LOG_FILE")"

{
    echo "=================================================="
    echo " System Report"
    echo "=================================================="
    echo "Student:        $STUDENT_NAME"
    echo "Date and time:  $(date '+%Y-%m-%d %H:%M:%S %Z')"
    echo "Username:       $(whoami)"
    echo "Hostname:       $(hostname)"
    echo

    echo "--- Operating system and kernel ---"
    if [ "$(uname -s)" = "Darwin" ]; then
        echo "OS:     $(sw_vers -productName) $(sw_vers -productVersion)"
    elif [ -f /etc/os-release ]; then
        echo "OS:     $(. /etc/os-release && echo "$PRETTY_NAME")"
    else
        echo "OS:     $(uname -s)"
    fi
    echo "Kernel: $(uname -sr)"
    echo "Arch:   $(uname -m)"
    echo

    echo "--- CPU ---"
    if [ "$(uname -s)" = "Darwin" ]; then
        echo "Model: $(sysctl -n machdep.cpu.brand_string)"
        echo "Cores: $(sysctl -n hw.ncpu)"
    else
        echo "Model: $(grep -m1 'model name' /proc/cpuinfo | cut -d: -f2 | sed 's/^ //')"
        echo "Cores: $(nproc)"
    fi
    echo

    echo "--- Memory ---"
    if command -v free >/dev/null 2>&1; then
        free -h
    else
        echo "Total: $(( $(sysctl -n hw.memsize) / 1024 / 1024 / 1024 )) GB"
        vm_stat | head -6
    fi
    echo

    echo "--- Disk usage ---"
    df -h
    echo

    echo "--- Available disk space on / ---"
    df -h / | awk 'NR==2 {print "Size: "$2"  Used: "$3"  Available: "$4"  Use%: "$5}'
    echo
    echo "Report finished for $STUDENT_NAME"
    echo
} | tee -a "$LOG_FILE"
