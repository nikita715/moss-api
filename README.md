[![Docker Cloud Build Status](https://img.shields.io/docker/cloud/build/nikstep/gitplag.svg)](https://hub.docker.com/r/nikstep/moss-api)

# moss-api

A service for parsing of [Moss](http://moss.stanford.edu) plagiarism analysis results.

You can launch it in [Docker](https://www.docker.com/) from the [image](https://hub.docker.com/r/nikstep/moss-api) and send there ids of moss analysis results to extract the analysis data in [json](http://www.json.org/) format.

Data can be formatted in two types:

- Simple type with pairs of files and their similarity


The service is launched at `localhost:4567`, but you can change the port by setting `MOSSPARSER_PORT` environment variable in docker container.

### API
- `localhost:4567/parse/{moss_result_id}`
- `localhost:4567/parse?url={moss_result_url}`
- `localhost:4567/parse-graph/{moss_result_id}`
- `localhost:4567/parse-graph?url={moss_result_url}`

You can add url parameter `mode=full` to each route to extract matched lines of analysed files.
