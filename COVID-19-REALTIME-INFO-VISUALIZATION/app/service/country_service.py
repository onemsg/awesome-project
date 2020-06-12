import os
import urllib3
from urllib3 import response
import json


def getAllCountryFromWeb() -> dict:

    http = urllib3.PoolManager()
    url = "corona.lmao.ninja/v2/countries"
    r: response.HTTPResponse = http.request("GET", url)
    print("code of response: ".format(r.status))
    print("type of r: {}".format(type(r)))
    print("type of r.data: {}".format(type(r.data)))
    data = json.loads(r.data.decode("utf-8"))

    countries = {}
    for country in data:
        name = country['country']
        info = {
            "iso2": country['countryInfo']['iso2'],
            "iso3": country['countryInfo']['iso3']
        }
        countries[name] = info
    return countries

path = os.path.join(os.sep.join(__file__.split(os.sep)[:-1]), "countries.json")
COUNTRIES = None
with open(path, "r", encoding='utf-8') as f:
    COUNTRIES = json.load(f)

# 包含所有国家名曾、iso2、iso3的数组
COUNTRIES_LIST = [c.upper() for c in COUNTRIES.keys()] + \
    [v for country in COUNTRIES.values() for v in country.values()]

def isValidCountry(country:str) -> bool:
    return country.upper() in COUNTRIES_LIST