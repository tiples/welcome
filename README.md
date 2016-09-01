# tiples/welcome
A composable multi-tab webapp built on websockets and [Hoplon](https://github.com/hoplon/hoplon#readme).

The welcome program will let you login as Sam, Fred or Kris and then displays "Welcome {Sam|Fred|Kris}"
along with a number of tabs.
Access control on the server determines which tabs are available for each user.
Sam and Fred have access to the welcome, profile and contacts tabs, while Kris can only access
the welcome and profile tabs.

For each tab there is some logic in the client and more in the server. A websocket is used for messaging
between the client and server. Either can send a message. And messages can be broadcast to all users
which have access to the same tab.

To run this program, enter the command "boot dev" from the welcome repository directory on your computer
and then point your browser to [http://localhost:8000/](http://localhost:8000/).

Account names (Sam, Fred, Kris) have the same password, 
but lowercase. I.E. "sam", "fred" and "kris" respectively.

See the [wiki](https://github.com/tiples/welcome/wiki) for more information.
