# Small sample app on modern android stack

## Stack
- Unidirectional Compose
- Coroutines and Flow
- Multi-module Hilt
- Room
- Retrofit
- Testing
  - junit
  - mockito
  - [turbine](https://github.com/cashapp/turbine)

## Structure
- `app` - UI and Android entry points
- `core/domain` - models, interfaces, business logic
- `core/db` - Room stuff
- `core/network` - Retrofit stuff

## Preview
![image](doc/screenshot.png)