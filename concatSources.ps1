

if (Test-Path "minitalk_sources.txt") {
    Remove-Item "minitalk_sources.txt"
}

Get-ChildItem -Recurse -Filter *.java -Path src\main\java |
ForEach-Object {
    Add-Content -Path minitalk_sources.txt -Value "=== FILE: $($_.FullName.Replace((Get-Location).Path + '\',''))"
    Get-Content $_.FullName | Add-Content -Path minitalk_sources.txt
    Add-Content -Path minitalk_sources.txt -Value ""
}

Get-ChildItem -Recurse -Filter *.mt -Path "stdlib" |
ForEach-Object {
    Add-Content -Path minitalk_sources.txt -Value "=== FILE: $($_.FullName.Replace((Get-Location).Path + '\',''))"
    Get-Content $_.FullName | Add-Content -Path minitalk_sources.txt
    Add-Content -Path minitalk_sources.txt -Value ""
}

Get-ChildItem -Recurse -Filter *.mt -Path "test" |
ForEach-Object {
    Add-Content -Path minitalk_sources.txt -Value "=== FILE: $($_.FullName.Replace((Get-Location).Path + '\',''))"
    Get-Content $_.FullName | Add-Content -Path minitalk_sources.txt
    Add-Content -Path minitalk_sources.txt -Value ""
}
