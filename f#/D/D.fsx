type Command =
    | Clone of Branch: string
    | Checkout of Branch: string * Reset: bool

let br = "sss"
let gb  = Clone br

type ConfigOption = {
    Key: string
    Value: string
}

type CommandLineOptions = {
    ConfigOptions: ConfigOption list
    Command: Command option
}

let emptyOptions = {
    ConfigOptions = []
    Command = None
}

// type Clone = { Branch: string }
// type Checkout = { Branch: string; Reset: bool }

let input = ["--config"; "test"; "1"; "checkout"; "origin/mybranch"]

let rec parseCommandLine(args: string list): CommandLineOptions =
    match args with
    | [] -> emptyOptions
    | "--config" :: tail ->
                        let key :: value :: rest = tail
                        let CmdOpts = parseCommandLine rest
                        let opts = {Key=key; Value=value} :: CmdOpts.ConfigOptions
                        {ConfigOptions=opts; Command=CmdOpts.Command}
    | "checkout" :: tail ->
                        let branch :: rest = tail
                        let reset = not rest.IsEmpty
                        let CmdOpts = parseCommandLine rest
                        {ConfigOptions=CmdOpts.ConfigOptions; Command=Some(Checkout(branch,reset))}
    | "clone" :: tail ->
                        let branch :: rest = tail
                        let CmdOpts = parseCommandLine rest
                        {ConfigOptions=CmdOpts.ConfigOptions; Command=Some(Clone branch)}

let compress args =
    parseCommandLine args
    |> string
    |> (fun f -> f.Replace("\n", ";").Replace(" ", ""))

printfn "%s" (compress input)