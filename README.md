# FSExec

A filesystem-based workflow/orchestration mini-engine to enable writing modularized, polylingual, and organized scripts for ETL, administration, or whatever.

The basic idea is that a filesystem and directory tree is designed to organize information in UNIX, so why not use a directory structure to represent the steps and stages to a workflow?

For example, imagine this unix command:

    cat <some file> | grep ccc | tr "[a-z]" "[A-Z]"

as this directory structure:

    [dump-filter-upcase]
      [01-dumpfile]
        cat-the-file.sh
      [02-filter]
        grep.perl
      [03-upcase]
        tr.sh
        
Now, I'm not advocating the reduction of all useful piped commands into FSExec flows, but one could see how something much more complicated would benefit from being broken down a bit.

See the Wiki for more. 

Send suggestions to gmail.
