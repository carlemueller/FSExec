package proto

File file = new File ("./proto-sample-data")

// sample input file
String rows = """aaa\tbbb\tc
111\tccc\t222
000\t---\tzzz
""";
file.write rows
11.times { file << rows }

println "done with sample data"

println "begin: cat proto-sample-data | grep c | grep 111"

Process catProc = "cat ./proto-sample-data".execute()
Process grepProcC = "grep c".execute()
Process grepProc111 = "grep 111".execute()

catProc.pipeTo(grepProcC)
grepProcC.pipeTo(grepProc111)


grepProc111.consumeProcessOutputStream(System.out)


// WORKS!

println "begin"

catProc = "cat ./proto-sample-data".execute()
grepProcC = "grep c".execute()
Process echoProc111 = "echo 111".execute()

catProc.pipeTo(grepProcC)
grepProcC.pipeTo(echoProc111)

echoProc111.consumeProcessOutputStream(System.out)

// WORKS, but the stream throws an I/O Exception

println "done"


