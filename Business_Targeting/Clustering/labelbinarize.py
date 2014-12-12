from sklearn import preprocessing

f = open('cat_list.csv', 'r')
r = f.read()
cat_list = []
content = r.splitlines()
for line in content:
	vals = line.split(',')
	vals = map(int, vals)
	cat_list.append(tuple(vals))
f.close()
mlb = preprocessing.MultiLabelBinarizer()
binarized_list = mlb.fit_transform(cat_list)
print(mlb.classes_)
print(binarized_list.shape)
f = open('bin_list.csv','w')
f.write(','.join(map(str, mlb.classes_)))
f.write('\n')
for item in binarized_list:
	f.write(','.join(map(str, item)))
	f.write('\n')
f.close()
