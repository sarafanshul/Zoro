# Zoro

### How to setup :
- Backend 
	- Clone [Luffy](https://github.com/sarafanshul/Luffy) for backend (not necessary if using compose).
	- If not cloned , copy the `compose file` from [docker/anshulsaraf/luffy](https://hub.docker.com/repository/docker/anshulsaraf/luffy) into a folder 
	- Make sure [Docker](https://www.docker.com/) is running .
	- `cd` inside the folder where `compose file` is saved and run `docker compose up` 
	- Now your backend should be up 
- Frontend
	- Inside `NetworkingConstants.kt` configure your backend constants
	- Replace the keystore  from `app/keystore/ZoroKeyStore.jks`with your signed one.
	- Setup Detekt (not forced but recommended)
	- Setup Configs in `local.properties`
- Android Studio specific
	- Invalidate Cache (just to be safe)
	- Build and add magic
