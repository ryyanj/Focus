#!/usr/bin/python

import sys
import re
import yaml

url = sys.argv[1]
filename = sys.argv[2]
status = False

with open("/Users/rxs4498/watchservice/" + filename, 'r') as stream:
    try:
    	yamldata = yaml.safe_load(stream)
    	urlblacklist = yamldata['urlblacklist']
    	urlwhitelist = yamldata['urlwhitelist']
    except yaml.YAMLError as exc:
        print(exc)

if urlblacklist:
	for regex in urlblacklist.split():
		if re.match(regex,url):
			status = True
			break

if urlwhitelist:
	for regex in urlwhitelist.split():
		if re.match(regex,url):
			status = False
			break

print status