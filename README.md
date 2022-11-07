# PASS policy service

[![Build Status](https://travis-ci.com/OA-PASS/pass-policy-service.svg?branch=master)](https://travis-ci.com/OA-PASS/pass-policy-service)

Contains the PASS policy service, which provides an HTTP API for determining the policies applicable to a given Submission, as well as the repositories that must be deposited into in order to comply with the applicable policies.

See the [Documentation for the API](src/main/java/org/eclipse/pass/policy/services/README.md)

## Installation

The policy service is distributed and intended to be used as a [docker image](#Docker Image), but nevertheless can be used on a local machine if desired

If you have go installed, you can simply install the `pass-policy-service` executable via

    go get github.com/oa-pass/pass-policy-service/cmd/pass-policy-service

 This will install the binary to your `${GOPATH/bin}`.  If you have that in your `$PATH`, this is particularly convenient for building and running cli commands.

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
submission.  Documentation can be found in [the rule DSL docs](rule/README.md)

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
