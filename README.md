# tiples/welcome
A composable multi-tab webapp built on websockets and [Hoplon](https://github.com/hoplon/hoplon#readme).
Websockets make social applications like chat reasonably simple to implement, but they are also helpful
when you need to update the data being viewed by a user. But why use Hoplon?

Hoplon is a system for generating all the DOM elements you will need and then updating them as your data changes.
For the Welcome program, Hoplon generates all the views but the only visible view is the one the user has selected.
Hoplon uses [Javelin](https://github.com/hoplon/javelin) and provides a spreadsheet-like dataflow, which 
makes it easy to trigger changes to the view when either the user generates an event or when a message is
received from the server.

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
