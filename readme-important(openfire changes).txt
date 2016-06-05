Used Openfire server to create this application

Important changes that made in openfire to make it work.

1)Go into Server option then in Server Settings, then Select Subscription Properties and under Subscription Service settings, select DISABLED option. So we can handle all request manually.

	Server > Server Settings > Subscription Properties > Subscription Service Settings > Disbaled
	
2)Go into Server option then in Server Settings, then Select Offline Messages and under Offline Message Policy, select Store and then Always store, So you can store messages when user is not offline and also you can get time stamp of offline messages.

	Server > Server Settings > Offline Messages > Offline Message Policy > Select Store > Always Store 
	
3)Go into Server option then in Server Settings, then Select Client Connections and under Idle Connection Policy select Do not disconnect clients that are idle. So your connection will not disconnect.

	Server > Server Settings > Client Connections > Idle Connection Policy > Do not disconnect clients that are idle