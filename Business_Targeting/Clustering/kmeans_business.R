setwd('C:/Users/aswinbharadwaj/Documents/BigData')
b_data <- read.csv('business.csv')
kmeans_data <- data.frame(b_data$latitude,b_data$longitude)
#kmeans_data <- scale(kmeans_data)
# testing for number of clusters

wss <- (nrow(kmeans_data)-1)*sum(apply(kmeans_data,2,var))
for (i in 2:15) wss[i] <- sum(kmeans(kmeans_data, 
                                     centers=i)$withinss)
plot(1:15, wss, type="b", xlab="Number of Clusters",
     ylab="Within groups sum of squares")

# K-Means Cluster Analysis
fit <- kmeans(kmeans_data, 14) # 14 cluster solution
# get cluster means 
clusters<-aggregate(kmeans_data,by=list(fit$cluster),FUN=mean)
# append cluster assignment
mydata <- data.frame(kmeans_data, fit$cluster)
b_data$cluster <- mydata$fit.cluster
write.table(b_data, "location_cluster.csv", sep=",", row.names = FALSE, quote= FALSE)

