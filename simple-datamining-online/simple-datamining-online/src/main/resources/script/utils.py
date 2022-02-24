import os

def make_sql(i, file):
    name = file[:-5]
    return "INSERT INTO dataset VALUES ({}, '{}', '{}')".format(i, name, "/dataset/public/" + file)
print(os.getcwd())
files = os.listdir(r"E:\workspace\Java-workspace\SpringBoot-In-Action\simple-datamining-online\src\main\resources\dataset\public")
print(os.getcwd())
print(files)

with open("data.sql", "w") as f:
    i = 0
    for file in files:
        f.write(make_sql(i,file) + "\n")
        # print(make_sql(i,file))
        i += 1

