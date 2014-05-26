JAVA_INCLUDE=-I /usr/lib/jvm/java-7-openjdk/include/ -I /usr/lib/jvm/java-7-openjdk/include/linux
CFLAGS=$(JAVA_INCLUDE)
CC=g++

all: libllapi.so
	
test: main.c
	$(CC) org_sp_shcp_llapi_LLAPI.c $(CFLAGS)

llapi.o: org_sp_shcp_llapi_LLAPI.c org_sp_shcp_llapi_LLAPI.h
	$(CC) -c -o llapi.o org_sp_shcp_llapi_LLAPI.c $(CFLAGS)

libllapi.so: llapi.o
	$(CC) -shared -o libllapi.so llapi.o $(CFLAGS)

clean:
	rm *.o -f
	rm *.out -f
	rm *.so -f
	rm *.gch -f
