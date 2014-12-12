###########################################################################################
####### Script to plot time lines of GFR count for N patients selected randomly ###########
###########################################################################################

# The database has been ported to local MySQL so that I can create my own indexes depeding
# upon the queries to make them fast.
# The assumption is there is a local MySQL database with table cdr_gfr_result
# containing readings of only those patients who have a GFR test record

library(RMySQL);
mysql_host = '127.0.0.1';
mysql_dbname = 'datasciencedb';
mysql_user = 'username';
mysql_passwd = 'password';
mydb = dbConnect(MySQL(), user=mysql_user, password=mysql_passwd, dbname=mysql_dbname, host=mysql_host);
N = 200;

# Fetch N random patients
query = paste(c("select distinct(idperson) from cdr_gfr_result order by rand() limit ",N), collapse="");
rs = dbSendQuery(mydb, query);
p_ids = fetch(rs, n=-1);

require(ggplot2);
graph = ggplot();

p_data = list();
for (i in 1:N) {
  # Find GFR count timeseries for a given patient
  query = paste(c("select resultdate, resultvaluenum from cdr_lab_result where valuename like '%GFR%' and idperson = ", p_ids[i,1]), collapse="");
  rs = dbSendQuery(mydb, query);
  p_data[[i]] = fetch(rs, n=-1);
  if (nrow(p_data[[i]]) == 0)
    next;
  p_data[[i]]$Date <- as.Date(p_data[[i]]$resultdate, "%Y-%m-%d");
  
  # Plot timeseries graph for a given patient
  graph = graph + geom_line(data = p_data[[i]], aes_string(x='Date', y='resultvaluenum'));
}
print(graph);
dbDisconnect(mydb);
