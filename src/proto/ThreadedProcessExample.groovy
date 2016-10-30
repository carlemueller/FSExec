package proto

File file = new File ("./proto-sample-data")

// sample input file
String rows = """aaa\tbbb\tc
111\tccc\t222
000\t---\tzzz
""";
file.write rows
10000.times { file << rows }

// cat the file, grepping it for "ccc" rows

Thread catThread = new Thread()

PipedReader catOutputReader = new PipedReader()
PipedWriter catOutputWriter = new PipedWriter()


// TODO: finish

class CatThread implements Runnable
{
  PipedWriter pw;
  void run() {
    "cat proto-sample-data".execu
  }
}

class GrepThread implements Runnable
{
  PipedReader pr;
  void run() {

  }
}

