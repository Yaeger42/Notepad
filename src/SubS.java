class SubS
{
    static void preprocess_strong_suffix(int []shift, int []bpos,
                                         char []pat, int m)
    {

        int i = m, j = m + 1;
        bpos[i] = j;

        while(i > 0)
        { 

            while(j <= m && pat[i - 1] != pat[j - 1])
            { 

                if (shift[j] == 0)
                    shift[j] = j - i;

                j = bpos[j];
            } 

            i--; j--;
            bpos[i] = j;
        }
    }


    static void preprocess_case2(int []shift, int []bpos,
                                 char []pat, int m)
    {
        int i, j;
        j = bpos[0];
        for(i = 0; i <= m; i++)
        {
            if(shift[i] == 0)
                shift[i] = j; 

            if (i == j)
                j = bpos[j];
        }
    }


    static String search(char []text, char []pat)
    {
        String result = null;
        int s = 0, j;
        int m = pat.length;
        int n = text.length;

        int []bpos = new int[m + 1];
        int []shift = new int[m + 1];

        for(int i = 0; i < m + 1; i++)
            shift[i] = 0;


        preprocess_strong_suffix(shift, bpos, pat, m);
        preprocess_case2(shift, bpos, pat, m);

        while(s <= n - m) {
            j = m - 1;


            while (j >= 0 && pat[j] == text[s + j])
                j--;

            //System.out.println("Not found " );
            //break;
            if (j < 0) {

                System.out.printf("pattern occurs at shift = %d\n", s);
                System.out.println("new String(text) = " + new String(text));
                s += shift[0];
                result = new String(text);
            }
            else s += shift[j + 1];
        }
        return result;
    }

    public static void main(String[] args)
    {
        char []text = "ABAAAABAACD".toCharArray();
        char []pat = "ABAACZ".toCharArray();
        search(text, pat);
    }
}  