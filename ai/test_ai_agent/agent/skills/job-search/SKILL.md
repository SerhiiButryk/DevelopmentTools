---
name: job-search
description: Use this skill when searching for IT job vacancies in Ukrainian tech companies and major employers to ensure accurate and correct results.
---

# Company List

1. ELEKS 
2. EPAM
3. SoftServe
4. GlobalLogic
5. Ajax
6. Genesis
7. Intellias
8. NIX
9. N-iX
10. Sigma Software
11. DataArt IT
12. Hygge Software
13. Univisia
14. Google
15. Newsoft
16. Sombra
17. Luxoft
18. Indeema
19. Uklon

# Ukraine IT Company Career Portal

When you search for job vacancies from specific Ukrainian tech companies, startups, or global offices operating in Ukraine, you must not rely solely on consumer marketing domains (e.g., `.com.ua`) and you must use the official career portals listed below.

## Target Company Directory & Direct Career URLs

* **ELEKS**: `eleks.com/careers`
* **EPAM**: `epam.com/careers`
* **SoftServe**: `softserveinc.com/careers`
* **GlobalLogic**: `globallogic.com/ua/career-search/`
* **Ajax Systems**: `ajax.systems/careers`
* **Genesis**: `genesis.tech/careers`
* **Intellias**: `intellias.com/careers`
* **NIX**: `nixsolutions.com/careers`
* **N-iX**: `n-ix.com/careers`
* **Sigma Software**: `sigma.software/careers`
* **DataArt**: `dataart.team/en/job-board`
* **Hygge Software**: Search via `hyggesoftware.com` or local Ukrainian tech job boards.
* **Univisia**: Search via official site or local platforms.
* **Google** (Ukraine/Kyiv/Remote): `google.com/about/careers/applications/`
* **Newsoft**: Search via official company channels or DOU/Djinni.
* **Sombra**: `sombra.co/careers`
* **Luxoft**: `luxoft.com/careers`
* **Indeema**: `indeema.com/careers`
* **Uklon**: `careers.uklon.net`

## Mandatory Full List Sequential Search Protocol

If the user submits a broad query without specifying a company name, you must **NOT** skip the list. You are required to execute a systematic, sequential search across all companies in the Target List:

1. **Step-by-Step Iteration:** Go through the Company List strictly one by one.
2. **Execution:** For each company, make a targeted call using its IT Company Career Portal from 'Target Company Directory & Direct Career URLs'.
3. **Aggregation:** Collect valid vacancies from each company and compile them into a single comprehensive report at the very end.
4. **Resilience:** If a specific company's site times out or fails, log it as "No results / Site unavailable" and immediately proceed to the next company on the list.

## Fallback Search Strategy

If a company's direct career portal fails to load, returns a 404, or blocks automated lookup, immediately fallback to major Ukrainian IT job aggregators using operator queries:
1. **Djinni**: Search `site:djinni.co [Company Name]` (Best for direct tech hiring).
2. **DOU Jobs**: Search `site:jobs.dou.ua [Company Name]` (Best for Ukrainian IT market overview).
3. **Work.ua / Robota.ua**: Use for broader domestic vacancy checks if necessary.