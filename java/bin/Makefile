JAVA_INCLUDE=-I /opt/java6/include -I /opt/java6/include/linux
CFLAGS=$(JAVA_INCLUDE)
CC=g++

all: libllapi.so
	
test: main.c
	$(CC) main.c $(CFLAGS)

llapi.o: org_sp_shcp_llapi_LLAPI.c
	$(CC) -c -o llapi.o org_sp_shcp_llapi_LLAPI.c $(CFLAGS)

libllapi.so: llapi.o
	$(CC) -shared -o libllapi.so llapi.o $(CFLAGS)

clean:
	rm *.o -f
	rm *.out -f
	rm *.so -f
	rm *.gch -f
