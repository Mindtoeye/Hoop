hadoop dfs -rmr /user/martinv/output
rm -rf ~/11-741/output
mkdir ~/11-741/output
hadoop jar INHoopRemoteTask.jar INHoopRemoteTask -hadoop -datapath /user/martinv/Wiki-60/ -outputpath /user/martinv/output
