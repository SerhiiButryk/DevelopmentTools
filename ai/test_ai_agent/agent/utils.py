"""
    File contains some utility functions like logging.
"""

from datetime import datetime
import sys

from sqlalchemy import text

call_count={}
log_file = "log.txt"

def init_log() -> None:

    timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    message = f">>> Log initialized at {timestamp}\n"

    # Create or truncate the log file
    log_to_file(message, override=True)

def log(message: str, count: bool = False) -> None:

    caller_name = sys._getframe(1).f_code.co_name

    timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    
    if count:
        # Dubegging info 
        call_count[caller_name] = call_count.get(caller_name, 0) + 1

    if caller_name in call_count:
        formatted = f"[{timestamp}] [{caller_name} #{call_count[caller_name]}] {message}"
    else:
        formatted = f"[{timestamp}] [{caller_name}] {message}"    

    log_to_file(formatted + "\n")

def log_to_file(message: str, filename: str = log_file, override: bool = False) -> None:

    flags = "w" if override else "a"
    
    with open(filename, flags, encoding="utf-8") as f:
        f.write(message)    