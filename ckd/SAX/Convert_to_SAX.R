filenames <- list.files("/home/rahul/Development/TS_clusteringData/result/", pattern="*.csv", full.names=F)
ldf <- lapply(filenames, function(x) read.table(x, header = FALSE,sep = ","))
#ldf_small <- ldf[1:10]
#normalize
ldf_n<- lapply(ldf,function(x)
  data.Normalization (x,type="n1",normalization="row"))

#apply PAA
#paa <- lapply(ldf_n, function(x)  PAA(x, 10))
#apply SAX
res <- lapply(ldf_n, function(x) convert.to.SAX.symbol(x, 9))
#change directory
setwd("/home/rahul/Development/TS_clusteringData/SAX/")
#convert list to data frame
df <- data.frame(matrix(unlist(res), nrow=length(res), byrow=T))
row.names(df) <- filenames
#write to file
write.table(df, file="sax_9_n.csv",sep=",",na="NA",row.names = F,
            col.names = F)



