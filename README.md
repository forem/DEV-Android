# DEV Android 💝

This is the repo for the dev.to Android app. 

# Status: 

Released first version: https://play.google.com/store/apps/details?id=to.dev.dev_android

# Design ethose

We will grow to include more native code over time, but for now we are taking the approach of _native shell/web views_. This approach lost favor early in iOS days, but I believe it is a very valid approach these days. It is inspired by how Basecamp does things. Our tech stack is a bit different, but the ideas are the same. 

https://m.signalvnoise.com/basecamp-3-for-ios-hybrid-architecture-afc071589c25

https://signalvnoise.com/posts/3743-hybrid-sweet-spot-native-navigation-web-content

https://signalvnoise.com/posts/3766-hybrid-how-we-took-basecamp-multi-platform-with-a-tiny-team

https://www.youtube.com/watch?v=SWEts0rlezA

By leveraging webviews as much as possible, I think we can make this all pretty awesome and sync up with our web dev work pretty smoothly. And where it makes sense, we can re-implement certain things fully native, or build entirely native features. Life's a journey, not a destination.
