#!/usr/focusbin/python

import sys
import re
import yaml
import subprocess



filename = sys.argv[1]

with open("/Users/rxs4498/watchservice/" + filename, 'r') as stream:
    try:
        yamldata = yaml.safe_load(stream)
        appblacklist = yamldata['appblacklist']
    except yaml.YAMLError as exc:
        print(exc)

if appblacklist:
    for app in appblacklist.split():
        subprocess.call('./processkiller.sh ' + app,shell=True)
