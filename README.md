[![Docker Cloud Build Status](https://img.shields.io/docker/cloud/build/nikstep/gitplag.svg)](https://hub.docker.com/r/nikstep/moss-api)

# moss-api

A service for parsing of [Moss](http://theory.stanford.edu/~aiken/moss/) plagiarism analysis results.

You can launch it in [Docker](https://www.docker.com/) from the [image](https://hub.docker.com/r/nikstep/moss-api) and send there ids of moss analysis results to extract the analysis data in [json](http://www.json.org/) format.

Data can be formatted in two types:

- Simple type with pairs of files and their similarity (and matched lines if required);
- Graph type that can be used to build a graph of similarity. Fully compatible with [data2graph](https://github.com/tcibinan/data2graph).

The service is launched at `localhost:4567` in docker container.

### API
- `localhost:4567/parse/{moss_result_id}`
- `localhost:4567/parse?url={moss_result_url}`
- `localhost:4567/parse-graph/{moss_result_id}`
- `localhost:4567/parse-graph?url={moss_result_url}`

You can use `/parse` with url parameter `mode=full` to extract matched lines of analysed files.
