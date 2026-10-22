
@main
struct UnmanagedSample {

    // C style pointer from a Swift object using Unmanaged 
    static func main() {

        class TestClass {}

        let obj = TestClass()
        
        let pointer = Unmanaged.passUnretained(obj).toOpaque()

        // Output: 0x0000600001234560
        print(pointer)
    }

}

