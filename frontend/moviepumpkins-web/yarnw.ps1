. ../../devenv.ps1 ../../env.proj

if ( -Not (Test-Path env:YARN_VERSION) ) {
    Write-Error '$env:YARN_VERSION is missing!'
    exit 1
}

New-Item -ItemType Directory -Force -Path $env:CACHE/.node | Out-Null

$pwd=pwd
cd $env:CACHE/.node

npm list yarn@YARN_VERSION > $null

if ($LASTEXITCODE -ne 0) {
    npm install yarn@$env:YARN_VERSION
}

if ( $(yarn -version) -eq $env:YARN_VERSION ) {
    yarn set version $env:YARN_VERSION
}

cd $pwd
yarn @args