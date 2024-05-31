section .data
	x dd 0
	a1 dd 0
	t1 dd 0
	t2 dd 0
	b dd 0
	t3 dd 0
	t4 dd 0
	a dd 0
	t5 dd 0
	t6 dd 0
	t7 dd 0
	t8 dd 0
	res dd 0
	
section .text
	global _start
	
_start:
	mov eax, 0
	mov [x], eax
	mov eax, 1
	and eax, 0
	mov [t1], eax
	mov eax, [t1]
	mov [a1], eax
	mov eax, 4
	mov ebx, 3
	xor edx, edx
	idiv ebx
	mov [t2], edx
	mov eax, [t2]
	mov [b], eax
	mov eax, 1
	add eax, 2
	mov [t3], eax
	mov eax, [t3]
	sub eax, [b]
	mov [t4], eax
	mov eax, [t4]
	mov [a], eax
	mov eax, 9
	mov ebx, 8
	xor edx, edx
	idiv ebx
	mov [t5], eax
	mov eax, 4
	imul eax, 3
	mov [t6], eax
	mov eax, 1
	imul eax, [t5]
	mov [t7], eax
	mov eax, [t6]
	add eax, [t7]
	mov [t8], eax
	mov eax, [t8]
	mov [res], eax
	mov rax, 60
    mov rdi, 0
    syscall
