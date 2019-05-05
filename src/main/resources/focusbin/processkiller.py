#!/usr/focusbin/python

import sys
import os.path
import logging
import yaml
import subprocess


homeservices_path = sys.argv[1]
filename = sys.argv[2]


logging.basicConfig(filename=os.path.abspath(os.path.join(os.path.realpath(__file__), '../../focuslogs/processkiller.log')),level=logging.INFO)
logging.info(sys.path)
with open(homeservices_path + filename, 'r') as stream:
    try:
        yamldata = yaml.safe_load(stream)
        appblacklist = yamldata['appblacklist']
    except yaml.YAMLError as exc:
        print(exc)

if appblacklist:
    for app in appblacklist.split():
        subprocess.call('pkill ' + app,shell=True)
