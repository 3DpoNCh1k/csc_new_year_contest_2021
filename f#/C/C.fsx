let partition predicate lst =
    let T = List.fold (fun acc elem -> elem :: acc) [] (List.filter predicate lst) in
    let F = List.filter (fun x -> not(predicate x)) lst in
    F @ T


// printfn "%A" (partition (fun x -> x > 3) [5; 2; 1; 4])
// assert (partition (fun x -> x > 3) [5; 2; 1; 4] = [2; 1; 4; 5])
 
let lst = ["DRCM"; "POICS"; "FXL"; "VTEFEX"; "KDCFOSP"]
printfn "%A" (partition (fun s -> String.length s >= 4) lst)