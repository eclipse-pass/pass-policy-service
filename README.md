# PASS policy service

[![Build Status](https://travis-ci.com/OA-PASS/pass-policy-service.svg?branch=master)](https://travis-ci.com/OA-PASS/pass-policy-service)

Contains the PASS policy service, which provides an HTTP API for determining the policies applicable to a given Submission, as well as the repositories that must be deposited into in order to comply with the applicable policies.

## Web API

### Policies

The policy service has a `/policies` endpoint that determines the set of policies that are applicable to
a given submission.  Note:  The results may be dependent on _who_ submits the request.  For example, if
somebody from JHU invokes the policies endpoint, a general "policy for JHU employees" may be included in the results.

#### Policies Request

`GET /policy-service/policies?submission=${SUBMISSION_URI}`

or (with encoded submission=${SUBMISSION_URI}))

```HTTP
POST /policy-service/policies
Content-Type application/x-www-form-urlencoded
```

#### Policies Response

The response is a list of URIs to Policy resources, decorated with a `type` property:

```json
[
 {
   "id": "http://pass.local:8080/fcrepo/rest/policies/2d/...",
   "type": "funder"
 },
 {
   "id": "http://pass.local:8080/fcrepo/rest/policies/63/...",
   "type": "institution"
 }
]
```

### Repositories

The policy service has a `/repositories` endpoint that, for a given submission, calculates the repositories that may be
deposited into in order to satisfy any applicable policies for that submission.

#### Repositories Request

GET `/policy-service/repositories?submission=${SUBMISSION_URI}`
or, with urlencoded (with encoded submission=${SUBMISSION_URI}) as the body:

```HTTP
POST /policy-service/repositories
Content-Type: application/x-www-form-urlencoded
```

#### Repositories Response

Response an application/json document that lists repositories sorted into buckets, as follows:

```json
{
   "required": [
       {
           "url": "http://pass.local/fcrepo/rest/repositories/1",
           "selected": true
       }
   ],
   "one-of": [
       [
           {
               "url": "http://pass.local/fcrepo/rest/repositories/2",
               "selected": true
           },
           {
               "url": "http://pass.local/fcrepo/rest/repositories/3",
               "selected": false
           }
       ],
       [
           {
               "url": "http://pass.local/fcrepo/rest/repositories/4",
               "selected": true
           },
           {
               "url": "http://pass.local/fcrepo/rest/repositories/5",
               "selected": false
           }
       ]
   ],
   "optional": [
       {
           "url": "http://pass.local/fcrepo/rest/repositories/6",
           "selected": true
       }
   ]
}
```

In the above example:

* Repository 1 is required.
* At least one of repositories 2 and 3 are required, repository 2 should be the default choice.
* At least one of repositories 4 and 5 are required, repository 4 should be the default choice.
* Repository 6 is optional, but selected by default.

The json document contains the following fields:

* `required`: lists all repositories for which deposit is required
* `one-of`:  contains a list of lists of repositories.  Each top level list defines a group, from which one of the repositories listed within must be chosen.  Multiple groups may be returned, each containing independent lists of repositories.  These lists may intersect (i.e. the same repository can appear in multiple groups).
* `optional`:  lists all repositories for which deposit is completely optional

Repositories contained within each of the above lists are JSON objects containing the following fields:

* `url`: the URL to the repository resource in Fedora
* `selected`: optional field.  Specifies if the repository should be selected by default in the UI or not.

## Installation

The policy service is distributed and intended to be used as a [docker image](#Docker Image), but nevertheless can be used on a local machine if desired

Otherwise (e.g. for development) you can [build it](#building) from a local codebase

For help with commands, use

    pass-policy-service help

### validating

The policy service can be used to validate the policy rules configuration file

    pass-policy-service validate /path/to/file.json

If successful, it will print out a message and exit with code 0.

If not successful, it will print out validation errors and terminate with a nonzero code

## Configuration

Configuration is provided via a policy rules DSL file.  This is a JSON document that contains rules which govern which policies apply to a given
submission.  Documentation can be found in [the rule DSL docs](docs/rules.md)

An example of such configuration file can be found in the [test data](rule/testdata/good.json)

## Building

Building the policy service requires go 1.12 or later.

First, clone

    git clone https://github.com/OA-PASS/pass-policy-service.git

Then, you can build the executable (which will be placed at the root of the pass-policy-service directory) via

    go generate ./...
    go build ./cmd/pass-policy-service

Otherwise, you can install it to `${GOPATH/bin}` via

    go generate ./...
    go install ./cmd/pass-policy-service

The `go generate` command generates code which will embed the schema file in the resulting executable.  This only needs to be done
when producing an executable for distribution (e.g. it's not necessary for tests, or when using `go run`).  Once done, it doesn't need
to be run again unless the schema changes.

## Testing

To run unit tests, do

    go test ./...

For integration tests, you need to have Fedora running.  Use the provided `docker-compose` file to do that

   docker-compose up -d

Then, run with integration tests

    go test -tags=integration ./...

## Docker Image

There is a `Dockerfile` which defines an image containing the policy service, and a `docker-compose.yml` for building/pushing the image, as well
as running Fedora for the sake of integration tests.

To build the policy service image, do

    docker-compose build

ci is set up to automatically build and deploy an image to docker hub upon commit to `master`, tagged as `:latest`.  For tags pushed to GitHub, ci will automatically build and
deploy an image whose tag matches the git tag.

### Docker Configuration

The `POLICY_FILE` environment variable.  This points to a policy rules DSL file (accessible in the container, either built-in, or mounted)

Built-in policy files include `docker.json` (default, works in the `pass-docker` environment), and `aws.json` (works in an AWS environment).

Additional configuration is achieved via the following environment variables:

* `PASS_EXTERNAL_FEDORA_BASEURL`: External (public) Fedora PASS baseurl
* `PASS_FEDORA_BASEURL`: Internal (private) Fedora PASS baseurl
* `PASS_FEDORA_USER`: Username for basic auth to Fedora
* `PASS_FEDORA_PASSWORD`: Password for basic auth to Fedora
* `POLICY_SERVICE_PORT`: Port for policy service port (default is 0 for random)
