export type Json =
  | string
  | number
  | boolean
  | null
  | { [key: string]: Json | undefined }
  | Json[]

export type Database = {
  public: {
    Tables: {
      company: {
        Row: {
          id: number
          image_url: string | null
          name: string
        }
        Insert: {
          id?: number
          image_url?: string | null
          name: string
        }
        Update: {
          id?: number
          image_url?: string | null
          name?: string
        }
        Relationships: []
      }
      queue: {
        Row: {
          company_id: number
          estimated_wait_time: number | null
          url: string | null
        }
        Insert: {
          company_id: number
          estimated_wait_time?: number | null
          url?: string | null
        }
        Update: {
          company_id?: number
          estimated_wait_time?: number | null
          url?: string | null
        }
        Relationships: [
          {
            foreignKeyName: "queue_company_id_fkey"
            columns: ["company_id"]
            isOneToOne: true
            referencedRelation: "company"
            referencedColumns: ["id"]
          }
        ]
      }
      queuedetails: {
        Row: {
          joined_at: string
          queue_id: number
          user_profile_id: number
        }
        Insert: {
          joined_at?: string
          queue_id: number
          user_profile_id: number
        }
        Update: {
          joined_at?: string
          queue_id?: number
          user_profile_id?: number
        }
        Relationships: [
          {
            foreignKeyName: "queuedetails_queue_id_fkey"
            columns: ["queue_id"]
            isOneToOne: false
            referencedRelation: "queue"
            referencedColumns: ["company_id"]
          },
          {
            foreignKeyName: "queuedetails_user_profile_id_fkey"
            columns: ["user_profile_id"]
            isOneToOne: false
            referencedRelation: "user_profile"
            referencedColumns: ["user_profile_id"]
          }
        ]
      }
      user_profile: {
        Row: {
          chat_id: string
          company_id: number | null
          first_name: string
          is_restaurant_admin: boolean
          last_name: string | null
          phone_number: string | null
          user_id: string
          user_profile_id: number
        }
        Insert: {
          chat_id: string
          company_id?: number | null
          first_name: string
          is_restaurant_admin?: boolean
          last_name?: string | null
          phone_number?: string | null
          user_id: string
          user_profile_id?: number
        }
        Update: {
          chat_id?: string
          company_id?: number | null
          first_name?: string
          is_restaurant_admin?: boolean
          last_name?: string | null
          phone_number?: string | null
          user_id?: string
          user_profile_id?: number
        }
        Relationships: [
          {
            foreignKeyName: "user_profile_company_id_fkey"
            columns: ["company_id"]
            isOneToOne: false
            referencedRelation: "company"
            referencedColumns: ["id"]
          },
          {
            foreignKeyName: "user_profile_user_id_fkey"
            columns: ["user_id"]
            isOneToOne: true
            referencedRelation: "users"
            referencedColumns: ["id"]
          }
        ]
      }
    }
    Views: {
      [_ in never]: never
    }
    Functions: {
      get_admin_restaurant_details: {
        Args: {
          company_name: string
        }
        Returns: {
          id: number
          image_url: string
          name: string
          estimated_wait_time: number
          queue_length: number
        }[]
      }
      get_queue_users: {
        Args: {
          company_id: number
        }
        Returns: {
          first_name: string
          last_name: string
          phone_number: string
          joined_at: string
          position: number
        }[]
      }
      get_restaurant_details: {
        Args: {
          company_name: string
        }
        Returns: {
          id: number
          image_url: string
          name: string
          estimated_wait_time: number
          queue_length: number
        }[]
      }
    }
    Enums: {
      [_ in never]: never
    }
    CompositeTypes: {
      [_ in never]: never
    }
  }
}

export type Tables<
  PublicTableNameOrOptions extends
    | keyof (Database["public"]["Tables"] & Database["public"]["Views"])
    | { schema: keyof Database },
  TableName extends PublicTableNameOrOptions extends { schema: keyof Database }
    ? keyof (Database[PublicTableNameOrOptions["schema"]]["Tables"] &
        Database[PublicTableNameOrOptions["schema"]]["Views"])
    : never = never
> = PublicTableNameOrOptions extends { schema: keyof Database }
  ? (Database[PublicTableNameOrOptions["schema"]]["Tables"] &
      Database[PublicTableNameOrOptions["schema"]]["Views"])[TableName] extends {
      Row: infer R
    }
    ? R
    : never
  : PublicTableNameOrOptions extends keyof (Database["public"]["Tables"] &
      Database["public"]["Views"])
  ? (Database["public"]["Tables"] &
      Database["public"]["Views"])[PublicTableNameOrOptions] extends {
      Row: infer R
    }
    ? R
    : never
  : never

export type TablesInsert<
  PublicTableNameOrOptions extends
    | keyof Database["public"]["Tables"]
    | { schema: keyof Database },
  TableName extends PublicTableNameOrOptions extends { schema: keyof Database }
    ? keyof Database[PublicTableNameOrOptions["schema"]]["Tables"]
    : never = never
> = PublicTableNameOrOptions extends { schema: keyof Database }
  ? Database[PublicTableNameOrOptions["schema"]]["Tables"][TableName] extends {
      Insert: infer I
    }
    ? I
    : never
  : PublicTableNameOrOptions extends keyof Database["public"]["Tables"]
  ? Database["public"]["Tables"][PublicTableNameOrOptions] extends {
      Insert: infer I
    }
    ? I
    : never
  : never

export type TablesUpdate<
  PublicTableNameOrOptions extends
    | keyof Database["public"]["Tables"]
    | { schema: keyof Database },
  TableName extends PublicTableNameOrOptions extends { schema: keyof Database }
    ? keyof Database[PublicTableNameOrOptions["schema"]]["Tables"]
    : never = never
> = PublicTableNameOrOptions extends { schema: keyof Database }
  ? Database[PublicTableNameOrOptions["schema"]]["Tables"][TableName] extends {
      Update: infer U
    }
    ? U
    : never
  : PublicTableNameOrOptions extends keyof Database["public"]["Tables"]
  ? Database["public"]["Tables"][PublicTableNameOrOptions] extends {
      Update: infer U
    }
    ? U
    : never
  : never

export type Enums<
  PublicEnumNameOrOptions extends
    | keyof Database["public"]["Enums"]
    | { schema: keyof Database },
  EnumName extends PublicEnumNameOrOptions extends { schema: keyof Database }
    ? keyof Database[PublicEnumNameOrOptions["schema"]]["Enums"]
    : never = never
> = PublicEnumNameOrOptions extends { schema: keyof Database }
  ? Database[PublicEnumNameOrOptions["schema"]]["Enums"][EnumName]
  : PublicEnumNameOrOptions extends keyof Database["public"]["Enums"]
  ? Database["public"]["Enums"][PublicEnumNameOrOptions]
  : never
