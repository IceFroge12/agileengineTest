Build: _**`mvn clean compile assembly:single`**_

The latest log trace is desired element.\
For file **_sample-1-evil-gemini.html_** - `" #page-wrapper > div.row:nth-child(3) > div.col-lg-8 > div.panel.panel-default > div.panel-body > a.btn.btn-success ", equal score = 10`

For file **_sample-2-container-and-clone.html_** - `" #page-wrapper > div.row:nth-child(3) > div.col-lg-8 > div.panel.panel-default > div.panel-body > div.some-container > a.btn.test-link-ok ", equal score = 10`

For file **_sample-3-the-escape.html_** - `" " #page-wrapper > div.row:nth-child(3) > div.col-lg-8 > div.panel.panel-default > div.panel-footer > a.btn.btn-success ", equal score = 10`

For file **_sample-4-the-mash.html_** - `" #page-wrapper > div.row:nth-child(3) > div.col-lg-8 > div.panel.panel-default > div.panel-footer > a.btn.btn-success ", equal score = 10`
