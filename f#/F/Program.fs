
// For more information see https://aka.ms/fsharp-console-apps

// dotnet run Program.fs

open FSharp.Data

type SibSUTI = 
    HtmlProvider<
        "https://sibsutis.ru/applicants/abiturient/places/">

let my_sample = SibSUTI.GetSample ()    
let my_seq = 
    my_sample.Tables.``Количество мест``.Rows
    |> Seq.groupBy (fun row ->
        row.``Направление подготовки, специальность``)
    |> Seq.map (fun group ->
        let key, rows = group
        let s = 
            rows
            |> Seq.map (fun row ->
                row.``бюджетные места - Всего бюджетных мест``,
                row.``План приема на платные места (на договорной основе)``)
            |> Seq.map (fun (budget, paid) ->
                (if budget.HasValue then budget.Value else 0) + paid)
            |> Seq.sum
        let t =
            rows
            |> Seq.map (fun row ->
                let comp = row.``бюджетные места - По общему конкурсу``
                if comp.HasValue then comp.Value else 0)
            |> Seq.sum
        t, s)

let ans =
    (my_seq
    |> Seq.map(fun (a,b) -> float(a) / float(b))
    |> Seq.sum)
    /
    float(Seq.length my_seq)

System.Console.WriteLine ans