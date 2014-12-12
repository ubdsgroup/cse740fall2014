diss_matrix <- read.csv(file="/home/rahul/Development/TS_clusteringData/SAX/sax_3_n.csv",head=FALSE,sep=",")
d <- daisy(diss_matrix, metric = "euclidean", stand = TRUE)
d[is.na(d)] <- 0
hc <- hclust(d, method = "complete", members = NULL)
memb <- cutree(hc, k = 5)
cent <- NULL
for(k in 1:5)
  cent <- rbind(cent, colMeans(diss_matrix[memb == k, , drop = FALSE]))
}
plot(hc)
