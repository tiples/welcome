# tiples/welcome
A composable multi-tab webapp built on websockets and [Hoplon](https://github.com/hoplon/hoplon#readme).

The welcome program will let you login as either Sam or Fred and then displays "Welcome {Sam|Fred}".
You can then select the tab/capability of interest: welcome, profile or contacts.
These capabilities vary based on user name, e.g. Kris has no access to contacts.

To run this program, enter the command "boot dev" from the welcome repository directory on your computer
and then point your browser to [http://localhost:8000/](http://localhost:8000/).

Account names (Sam, Fred, Kris) have the same password, 
but lowercase. I.E. "sam", "fred" and "kris" respectively.

See the [wiki](https://github.com/tiples/welcome/wiki) for more information.
