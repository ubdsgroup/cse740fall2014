'''
Created on Nov 29, 2014

@author: rahul
'''
import csv
import psycopg2

conn_string = "host='localhost' dbname='datasciencedb' user='postgres' password=''"
# print the connection string we will use to connect
print "Connecting to database\n    ->%s" % (conn_string)

# get a connection, if a connect cannot be made an exception will be raised here
conn = psycopg2.connect(conn_string)

# conn.cursor will return a cursor object, you can use this cursor to perform queries
cursor = conn.cursor()
#writing complex queries in parts
query_p1 = """ SELECT * FROM crosstab(
                'SELECT resultdate,valuename, resultvaluenum
                FROM cdr_lab_result
                where valuename like ''%LR_GFR%'' and idperson ="""

query_p2= """ order by resultdate')
                AS cdr_lab_result (resultdate timestamp without time zone, resultvaluenum numeric(9,3));"""



count = 1
with open('/home/rahul/Development/TS_clusteringData/patients.csv', 'r') as f:
        reader = csv.reader(f)
        for row in reader:
            patient_id = row[0]
            cursor.execute (query_p1 + str(patient_id) + query_p2)
            print str(count) + ":  writing data for: " + str(patient_id)
            csv_writer = csv.writer(open("/home/rahul/Development/TS_clusteringData/all_patients/"+patient_id+ ".csv", "wt"))
            #csv_writer.writerow([i[0] for i in cursor.description]) # write headers
            csv_writer.writerows(cursor)
            count+=1
            del csv_writer