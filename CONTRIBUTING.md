# Contributing to AMRIT

AMRIT is released under the GPLv3 license. If you would like to contribute something, or want to hack on the code this document should help you get started.

# Code of Conduct
This project adheres to the [Contributor Covenant](CODE_OF_CONDUCT.md).
By participating, you are expected to uphold this code. Please report unacceptable behavior to amrit@piramalswasthya.org.

# Using GitHub Issues
We use GitHub issues to track bugs and enhancements reported by the community.
If you have a general usage question please ask on amrit@piramalswasthya.org.

If you are reporting a bug, please help to speed up problem diagnosis by providing as much information as possible.
Ideally, that would include a small sample project that reproduces the problem.

# Reporting Security Vulnerabilities
If you think you have found a security vulnerability in AMRIT please *DO NOT* disclose it publicly until we've had a chance to fix it.
Please don't report security vulnerabilities using GitHub issues, instead drop an email to amrit@piramalswasthya.org by providing as much as information as possible.

# Code Conventions and Housekeeping
None of these are essential for a pull request, but they will all help.  They can also be added after the original pull request but before a merge.

* Make sure all new `.java` files have a Javadoc class comment with at least an `@author` tag identifying you, and preferably at least a paragraph on what the class is for.
* Add the GPLv3 boilerplate notice (license header comment) to all new `.java` files (copy from existing files in the project).
* Add yourself as an `@author` to the `.java` files that you modify substantially (more than cosmetic changes).
* Add some Javadocs.
* A few unit tests would help a lot as well -- someone has to do it.
* If no-one else is using your branch, please rebase it against the current main branch (or other target branch in the project).
* When writing a commit message please follow [these conventions](https://tbaggery.com/2008/04/19/a-note-about-git-commit-messages.html).

# Working with the Code
For information on editing, building, and testing the code, see the [developer guide](https://psmri.github.io/PSMRI) page on the project wiki.
