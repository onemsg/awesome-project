import os

imgs_path = 'src\\main\\webapp\\images'

files = os.listdir(imgs_path)

def make_insert_sql(file):
    name = file.split(".")[0]
    return "INSERT INTO `amazing`.`item_imgs` (`url`, `item_id`) VALUES ('{}', '{}');".format(name, file)

with open('src\\main\\resources\\script\\insert.sql', 'w') as f:
    for _ in files:
        sql = make_insert_sql(_)
        f.write(sql + "\n")

print("writte over")

