# Mac C/C++ Debugger

------- Tell lldb where the source code is. The debbuger will use it for all matched references -------
$ (lldb) settings set target.source-map /build/dir/path /my/local/source/path

------- List all shared libraries associated with the current target -------
$ (lldb) image list

------- Find specific shared library associated with the current target -------
$ (lldb) image list libmylib.so

------- Check if shared library has debug symbols for specific file -------

1. Find location of your llvm toolchain.

Example: /home/serhii/Android/Sdk/ndk/23.1.7779620/toolchains/llvm/prebuilt/linux-x86_64/bin

2. Find location of your .so library you want to chack
3. Run command and specify file name which you want to check

Example:
/home/serhii/Android/Sdk/ndk/23.1.7779620/toolchains/llvm/prebuilt/linux-x86_64/bin/llvm-dwarfdump /path/to/your/sharedlibrary/my.so | grep file_name_for_check.cpp

------- Set breakpoints -------

$ (lldb) breakpoint set -n function_name
$ (lldb) breakpoint set -f file_name.cpp -l line_number

$ (lldb) breakpoint list

# Symbol lookup
$ (lldb) image lookup -vn symbol

# Dump all threads
$ (lldb) bt all

# Log thread frames
$ (lldb) thread backtrace

# Inspect simple variables
$ (lldb) p <var>
$ (lldb) po <obj>
