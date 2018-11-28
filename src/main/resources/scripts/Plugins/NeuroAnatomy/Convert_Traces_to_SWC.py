#@String(value="This script converts all .traces files in a directory into SWC. Conversion log is shown in Console.", visibility="MESSAGE") msg
#@File(label="Directory of .traces files:", style="directory") input_dir
#@Boolean(label="Open Console", value=false) open_console
#@LogService log
#@StatusService status
#@UIService ui

'''
file:       Convert_Traces_to_SWC.py
author:     Tiago Ferreira
version:    20181128
info:       Converts all .traces files in a directory into SWC
'''

import os, re
from tracing import PathAndFillManager

def run():
    if not input_dir:
        return
    status.showStatus("Converting .traces files...")
    if open_console:
        ui.getDefaultUI().getConsolePane().show()
    conversion_counter = 0
    d = str(input_dir)
    log.info('Processing %s...' % d)
    for f in os.listdir(d):
        if os.path.basename(f).startswith('.'):
            continue
        if re.search('.*-exported-\d{3}.swc', f):
            log.warn("'%s' already exists" % f)
        if not f.lower().endswith('.traces'):
            log.info('Skipping %s...' % f)
            continue
        file_path = os.path.join(d, f)
        swc_filename_prefix = re.sub(r'\.traces', '-exported', file_path)
        log.info('Converting %s to %s-*.swc' % (file_path, swc_filename_prefix))
        pafm = PathAndFillManager.createFromFile(file_path)
        if pafm.exportAllPathsAsSWC(swc_filename_prefix):
            conversion_counter += 1
        else:
            log.error('Could not convert %s' % file_path)
    log.info(str(conversion_counter) + ' file(s) successfully converted')

run()
