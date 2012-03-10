hadoop dfs -rmr /user/hduser/output
rm -rf ./hdoutput
mkdir hdoutput
hadoop jar INMain.jar INMain -task invert -shards 5 -splitsize 10 -shardcreate mos -shardtype docid -dbglocal yes -hadoop -datapath /user/hduser/text-10/ -outputpath /user/hduser/output
