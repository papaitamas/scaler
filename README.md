Optimized for running locally in docker + k8
No tests were created, that was not part of the task list
All the Get / Get all / Create / Update / Patch/ Delete endpoints are implemented
Endpoints are secured be JWT token. Roles should be READ / WRITE. With WRITE you can do everything, with READ role you can run only the getters.
Special search endpoint for meetings is a POST endpoint. It is better format than GET, no needs to set all the possible parameters in the URL, just set the necessary ones in the request body
Meetings could be finalized
Finalized meetings cannot be updated, deleted or patched.
