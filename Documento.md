Documento Técnico: Rediseño y Auditoría del Sistema Bancario Legacy
1. Introducción

Se realizó una auditoría al código del sistema bancario Legacy con el objetivo de identificar problemas relacionados con el manejo de memoria y mejorar su diseño utilizando programación orientada a objetos en Java.

El código original utilizaba memoria dinámica mediante punteros y arreglos de caracteres, lo que podía generar fugas de memoria y errores en el manejo de los datos. Por esta razón, se realizó un rediseño utilizando clases abstractas, herencia, encapsulamiento y manejo automático de recursos.

2. Hallazgos en el código Legacy

Uno de los principales problemas encontrados estaba en el uso de memoria dinámica:

CuentaLegacy* c = new CuentaLegacy();
c->titular = new char[50];

Aquí se reservaba memoria para la cuenta y también para almacenar el nombre del titular.

El problema era que no existía una liberación adecuada de esta memoria. Si se eliminaba la cuenta sin utilizar:

delete[] c->titular;
delete c;

la memoria utilizada para el titular podía quedar sin liberar, generando una fuga de memoria.

Además, se utilizaba:

strcpy(c->titular, nombre);

lo cual podía provocar un desbordamiento de memoria si el nombre recibido tenía más caracteres de los permitidos en los 50 espacios reservados.

3. Problema del diseño Legacy

También se encontró que la estructura utilizaba:

int tipoCuenta;
double limiteSobregiro;

El atributo tipoCuenta se utilizaba para determinar si la cuenta era de ahorros o corriente.

Esto hacía que la clase tuviera que conocer y controlar diferentes tipos de cuenta mediante números. Además, limiteSobregiro estaba presente incluso en cuentas de ahorro, aunque estas no lo necesitaban.

Esto representaba un diseño poco flexible y hacía que las clases tuvieran atributos que realmente no les correspondían.

4. Rediseño de la clase CuentaBancaria

Para solucionar estos problemas se creó una clase abstracta llamada CuentaBancaria.

Se dejaron únicamente los atributos que son comunes para cualquier cuenta:

titular
saldo

Estos atributos se declararon como private para aplicar encapsulamiento.

También se crearon sus respectivos getters y setters.

El atributo tipoCuenta fue eliminado porque ya no es necesario. El tipo de cuenta se determina directamente por la clase que se está utilizando.

Por ejemplo:

CuentaAhorros
CuentaCorriente

Esto permite que la clase padre no tenga que utilizar números como 1 o 2 para identificar el tipo de cuenta.

5. Uso de herencia y polimorfismo

Se crearon las clases:

CuentaAhorros
CuentaCorriente

ambas heredando de:

CuentaBancaria

Cada clase implementa su propia versión del método:

aplicarComisionMensual()

utilizando:

@Override

Esto permite aplicar diferentes reglas dependiendo del tipo de cuenta.

Además, el atributo limiteSobregiro fue trasladado únicamente a CuentaCorriente, porque es la única cuenta que necesita este atributo.

De esta manera se evita guardar información innecesaria en las cuentas de ahorro.

6. Manejo de memoria

Una de las principales mejoras del rediseño fue reemplazar el uso de:

char* titular;

por:

private String titular;

En Java no es necesario reservar y liberar manualmente la memoria para almacenar el nombre.

Esto elimina los problemas que existían con:

new char[50];
strcpy();
delete[];

El manejo de memoria de los objetos queda administrado por Java y su Garbage Collector, reduciendo considerablemente el riesgo de fugas de memoria provocadas por una liberación manual incorrecta.

7. Manejo de recursos y auditoría

También se creó la clase:

RegistroAuditoriaBancaria

que implementa:

AutoCloseable

Esta clase permite registrar operaciones bancarias en un archivo.

Para garantizar que el archivo se cierre correctamente se utiliza try-with-resources:

try (RegistroAuditoriaBancaria auditoria =
         new RegistroAuditoriaBancaria("auditoria.log")) {

    auditoria.registrar("Cuenta creada correctamente.");

}

Al finalizar el bloque try, Java ejecuta automáticamente el método close().

Esto permite liberar el recurso inmediatamente y no depender del Garbage Collector para cerrar el archivo.

8. Beneficios del rediseño

El nuevo diseño presenta varias mejoras:

Se eliminan las fugas de memoria relacionadas con el manejo manual de char*.
Se evita el uso inseguro de strcpy.
Se aplica encapsulamiento mediante atributos privados.
Se utiliza herencia para representar los diferentes tipos de cuenta.
Se utiliza polimorfismo mediante aplicarComisionMensual().
Se eliminan atributos innecesarios de la clase padre.
limiteSobregiro pertenece únicamente a CuentaCorriente.
Se utiliza AutoCloseable para controlar los recursos de auditoría.
Se utiliza try-with-resources para garantizar el cierre automático de los archivos.
El código queda más organizado, mantenible y fácil de ampliar.
9. Conclusión

La auditoría permitió identificar principalmente problemas de manejo manual de memoria y un diseño poco flexible en el código Legacy.

El rediseño en Java permite solucionar estos problemas utilizando las características de la programación orientada a objetos. La clase CuentaBancaria contiene únicamente los elementos comunes, mientras que las clases hijas manejan las características específicas de cada tipo de cuenta.

Además, el uso de String, herencia, polimorfismo, AutoCloseable y try-with-resources hace que el sistema sea más seguro y fácil de mantener.

En conclusión, el nuevo diseño reduce los riesgos de fugas de memoria y mejora la organización del sistema sin mantener las limitaciones innecesarias del código Legacy.
