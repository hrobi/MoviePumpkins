$env_proj = if($args.Count -gt 0) { $args[0] } else { "env.proj" }

if(-Not (Test-Path $env_proj -PathType Leaf)) {
    Write-Error "ERROR: $env_proj file not found!"
    exit 1
}

foreach($line in Get-Content $env_proj) {
    if($line.StartsWith("#") -or -Not $line.Trim()) {
        continue
    }


    $key, $value = $line.Split("=")
    $new_value = $value.Replace('$HOME', "$HOME")
    $new_value = [regex]::Replace($new_value, '\$([\w]+)', {param($match) [System.Environment]::GetEnvironmentVariable($match.Groups[1].Value)})
    [System.Environment]::SetEnvironmentVariable($key, $new_value)
}

if(-Not (Test-Path env:CACHE)) {
    Write-Error 'ERROR: $env:CACHE property is not defined!'
    exit 1
}

New-Item -ItemType Directory -Force -Path $env:CACHE | Out-Null

function Download-Url($url) {
    $file = $url.Split("/")[-1]
    if(Test-Path $env:CACHE/$file) {
        return
    }

    wget $url -OutFile $env:CACHE/$file

    if($file -match "\.zip$") {
       mkdir "$env:CACHE/$file.dir"
       Expand-Archive $env:CACHE/$file -DestinationPath "$env:CACHE/$file.dir"
       rm $env:CACHE/$file
       Rename-Item -Path "$env:CACHE/$file.dir" -NewName "$env:CACHE/$file"
    }
}

function Add-To-Path($bin_path) {
    if($env:Path.Contains("$bin_path;")) {
        return
    }
    $env:Path = "$bin_path;$env:Path"
}

foreach($line in Get-Content $env_proj) {
    if($line.StartsWith("#") -or -Not $line.Trim()) {
        continue
    }


    $key, $_ = $line.Split("=")

    switch($key) {
        { $_ -match "_URL$" } { Download-Url $([System.Environment]::GetEnvironmentVariable($key)) }
        { $_ -match "_BIN$" } { Add-To-Path $([System.Environment]::GetEnvironmentVariable($key)) }
        { $_ -match "_DIR$" } { New-Item -ItemType Directory -Force -Path $([System.Environment]::GetEnvironmentVariable($key)) | Out-Null }
    }
}
