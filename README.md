# smart-saving-contract
smart-saving-contract


#Generate .avn contract file
Make sure you have [dotnet-sdk](https://www.microsoft.com/net/core) installed!

Run `./gradlew compileJava`, this will compile .java contract file into .class java bytecode.
It will be in `build/classes` directory. 

Next, run from root project directory following command`dotnet run --project ./neoj/neoj.csproj ./build/classes/java/main/com/blocksaver/contract/SmartSavingContract.class`.


Note: i will write gradle task to automate this things...