#!/usr/focusbin/python

import sys
import re
import yaml
import subprocess


homeservices_path = sys.argv[1]
filename = sys.argv[2]

with open(homeservices_path + filename, 'r') as stream:
    try:
        yamldata = yaml.safe_load(stream)
        appblacklist = yamldata['appblacklist']
    except yaml.YAMLError as exc:
        print(exc)

if appblacklist:
    for app in appblacklist.split():
        subprocess.call('pkill ' + app,shell=True)
