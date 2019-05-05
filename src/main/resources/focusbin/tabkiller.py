#!/usr/focusbin/python

import sys
import os.path
import re
import yaml

url = sys.argv[1]
homeservices_path = sys.argv[2]
filename = sys.argv[3]
status = False

with open(homeservices_path + filename, 'r') as stream:
    try:
    	yamldata = yaml.safe_load(stream)
    	urlblacklist = yamldata['urlblacklist']
    	urlwhitelist = yamldata['urlwhitelist']
    except yaml.YAMLError as exc:
        print(exc)

if urlblacklist:
	for regex in urlblacklist.split():
		if re.search(regex,url):
			status = True
			break

if urlwhitelist:
	for regex in urlwhitelist.split():
		if re.search(regex,url):
			status = False
			break

print status