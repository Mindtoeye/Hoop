hadoop dfs -rmr /user/martinv/output
rm -rf ~/11-741/output
mkdir ~/11-741/output
hadoop jar INMain.jar INMain -task invert -shards 5 -splitsize 60000 -shardcreate mos -shardtype docid -hadoop -datapath /user/martinv/Wiki-60/ -outputpath /user/martinv/output
