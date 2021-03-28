
ON ERROR RESUME NEXT

' SALTO DE LINEA
set sl = chr(13) & chr(10) 

dim carpetas(3)

borradas = "Borradas correctamente:" & vbCrLf
noEncontradas = "No se han encontrado:" & vbCrLf

' CARPETAS A BORRAR
carpetas(0) = ".idea"
carpetas(1) = ".gradle"
carpetas(2) = "app\build"

' CLASE PARA TRABAJR CON EL SISTEMA DE ARCHIVOS
Set fs = CreateObject("Scripting.FileSystemObject")

' BORRAR CARPETAS
for i=0 to 2
    err.Clear()
    fs.DeleteFolder(carpetas(i))
    if (err.Description = "") then
        borradas = borradas & "   " + carpetas(i)  & vbCrLf
    else
        noEncontradas = noEncontradas & "   " & carpetas(i) & vbCrLf
    end if
next

if (UBound(split(borradas, vbCrLf)) > 2) then
    msg = msg & borradas
end if

if (UBound(split(noEncontradas, vbCrLf)) > 2) then
    msg = msg & noEncontradas
end if

' INDICAR QUE CARPETAS SE HAN PODIDO BORRAR
MsgBox(msg)
