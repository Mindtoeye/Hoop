hadoop dfs -rmr /user/martinv/output
rm -rf ~/11-741/output
mkdir ~/11-741/output
hadoop jar INHoopRemoteTask.jar INHoopRemoteTask -task invert -shards 5 -splitsize 10 -shardcreate mos -shardtype docid -hadoop -datapath /user/martinv/Wiki-10/ -outputpath /user/martinv/output
